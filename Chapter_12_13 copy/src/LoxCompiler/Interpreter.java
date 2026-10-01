package src.LoxCompiler;

import java.lang.classfile.Superclass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import src.LoxCompiler.Expr.Assign;
import src.LoxCompiler.Expr.Call;
import src.LoxCompiler.Expr.Logical;
import src.LoxCompiler.Expr.Super;
import src.LoxCompiler.Expr.Variable;
import src.LoxCompiler.Stmt.Block;
import src.LoxCompiler.Stmt.Break;
import src.LoxCompiler.Stmt.Function;
import src.LoxCompiler.Stmt.Return;

class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private List<Stmt> evaluatedExpressionStatements = new ArrayList<>();
    private static class BreakException extends RuntimeException {}
    private final Map<Expr, Integer> locals = new HashMap<>();

    final Environment globals = new Environment();
    private Environment environment = globals;


    Interpreter() {
      globals.define("clock", new LoxCallable() {
        @Override
        public int arity() { return 0; }

        @Override
        public Object call(Interpreter interpreter,
                          List<Object> arguments) {
          return (double)System.currentTimeMillis() / 1000.0;
        }

        @Override
        public String toString() { return "<native fn>"; }
      });
    }

      @Override
  public Object visitLiteralExpr(Expr.Literal expr) {
    return expr.value;
  }

    @Override
  public Object visitGroupingExpr(Expr.Grouping expr) {
    return evaluate(expr.expression);
  }


  private Object evaluate(Expr expr) {
    return expr.accept(this);
  }


  @Override
  public Void visitExpressionStmt(Stmt.Expression stmt) {
    evaluatedExpressionStatements.add(stmt);
    evaluate(stmt.expression);
    return null;
  }


  public String stringExpression(Stmt.Expression stmt)
  {
    Object value = evaluate(stmt.expression);
    String stringExpression = stringify(value);
    return stringExpression;

  }

  @Override
  public Void visitPrintStmt(Stmt.Print stmt) {
    Object value = evaluate(stmt.expression);
    System.out.println(stringify(value));
    return null;
  }


  private void execute(Stmt stmt) {
    stmt.accept(this);
  }

  void resolve(Expr expr, int depth) {
    locals.put(expr, depth);
  }

  void executeBlock(List<Stmt> statements,
                    Environment environment) {
    Environment previous = this.environment;
    try {
      this.environment = environment;

      for (Stmt statement : statements) {
        execute(statement);
      }
    } finally {
      this.environment = previous;
    }
  }

    @Override
  public Object visitUnaryExpr(Expr.Unary expr) {
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {
      case BANG:
        return !isTruthy(right);
      case MINUS:
        return -(double)right;
    }
    // Unreachable.
    return null;
  }




private boolean isTruthy(Object object) {
    if (object == null) return false;
    if (object instanceof Boolean) return (boolean)object;
    return true;
  }





  @Override
  public Object visitBinaryExpr(Expr.Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right); // [left]

    switch (expr.operator.type) {
//> binary-equality
      case BANG_EQUAL: return !isEqual(left, right);
      case EQUAL_EQUAL: return isEqual(left, right);
//< binary-equality
//> binary-comparison
      case GREATER:
//> check-greater-operand
        checkNumberOperands(expr.operator, left, right);
//< check-greater-operand
        return (double)left > (double)right;
      case GREATER_EQUAL:
//> check-greater-equal-operand
        checkNumberOperands(expr.operator, left, right);
//< check-greater-equal-operand
        return (double)left >= (double)right;
      case LESS:
//> check-less-operand
        checkNumberOperands(expr.operator, left, right);
//< check-less-operand
        return (double)left < (double)right;
      case LESS_EQUAL:
//> check-less-equal-operand
        checkNumberOperands(expr.operator, left, right);
//< check-less-equal-operand
        return (double)left <= (double)right;
//< binary-comparison
      case MINUS:
//> check-minus-operand
        checkNumberOperands(expr.operator, left, right);
//< check-minus-operand
        return (double)left - (double)right;
//> binary-plus
      case PLUS:
        if (left instanceof Double && right instanceof Double) {
          return (double)left + (double)right;
        } // [plus]
        if (left instanceof String && right instanceof String) {
          return (String)left + (String)right;
        }
        if((left instanceof String && right instanceof Double))
        {
            String rightString = right.toString();
            String leftString = left.toString();
            String concatString = leftString.concat(rightString);
            Object obj = concatString;
            return obj;
        }
        if((left instanceof Double && right instanceof String))
        {
            String rightString = right.toString();
            String leftString = left.toString();
            String concatString = leftString.concat(rightString);
            Object obj = concatString;
            return obj;
        }

/* Evaluating Expressions binary-plus < Evaluating Expressions string-wrong-type
        break;
*/
//> string-wrong-type
        throw new RuntimeError(expr.operator,
            "Operands must be two numbers or two strings.");
//< string-wrong-type
//< binary-plus
      case SLASH:
//> check-slash-operand
        checkNumberOperands(expr.operator, left, right);
//< check-slash-operand
        return (double)left / (double)right;
      case STAR:
//> check-star-operand
        checkNumberOperands(expr.operator, left, right);
//< check-star-operand
        return (double)left * (double)right;
    }

    // Unreachable.
    return null;
  }

  private String stringify(Object object) {
    if (object == null) return "nil";

    if (object instanceof Double) {
      String text = object.toString();
      if (text.endsWith(".0")) {
        text = text.substring(0, text.length() - 2);
      }
      return text;
    }

    return object.toString();
  }


  //Challenge Problem 3 Chapter 7
private void checkNumberOperands(Token operator,
                                   Object left, Object right) {

  if (!(left instanceof Double && right instanceof Double)) {
    throw new RuntimeError(operator, "Operands must be numbers.");
  }
  if (operator.type == TokenType.SLASH && (double) right == 0) {
    throw new RuntimeError(operator, "Divisor can not be 0");
  }
  }


private boolean isEqual(Object a, Object b) {
    if (a == null && b == null) return true;
    if (a == null) return false;

    return a.equals(b);
  }



  void interpret(List<Stmt> statements) {
    try {
      for (Stmt statement : statements) {
        execute(statement);
      }
    } catch (RuntimeError error) {
      Lox.runtimeError(error);
    }
  }


//Chapter 10 Challenge problem 3
@Override
public Object visitAssignExpr(Assign expr) throws RuntimeException{
    Object value = evaluate(expr.value);
    Integer distance = locals.get(expr);
    if (distance != null) {
      if(value == null)
      {
        throw new RuntimeErrorException(new Error(), "Uninitialized variable: " + expr.toString());
      }
    environment.assignAt(distance, expr.name, value);
    } else {
      globals.assign(expr.name, value);
    }

    return value;
}



@Override
public Object visitCallExpr(Call expr) {
    Object callee = evaluate(expr.callee);

    List<Object> arguments = new ArrayList<>();
    for (Expr argument : expr.arguments) { 
      arguments.add(evaluate(argument));
    }

    if(!(callee instanceof LoxCallable))
    {
      throw new RuntimeError(expr.paren, "Can only call functions and classes");
    }

    LoxCallable function = (LoxCallable)callee;
      if (arguments.size() != function.arity()) {
      throw new RuntimeError(expr.paren, "Expected " +
          function.arity() + " arguments but got " +
          arguments.size() + ".");
    }
    return function.call(this, arguments);
}

  @Override
  public Object visitGetExpr(Expr.Get expr) {
    Object object = evaluate(expr.object);
    if (object instanceof LoxInstance) {
      return ((LoxInstance) object).get(expr.name);
    }

    throw new RuntimeError(expr.name,
        "Only instances have properties.");
  }

@Override
public Object visitLogicalExpr(Logical expr) {
    Object left = evaluate(expr.left);

    if (expr.operator.type == TokenType.OR) {
      if (isTruthy(left)) return left;
    } else {
      if (!isTruthy(left)) return left;
    }

    return evaluate(expr.right);
}

  @Override
  public Object visitSetExpr(Expr.Set expr) {
    Object object = evaluate(expr.object);

    if (!(object instanceof LoxInstance)) { 
      throw new RuntimeError(expr.name,
                             "Only instances have fields.");
    }

    Object value = evaluate(expr.value);
    ((LoxInstance)object).set(expr.name, value);
    return value;
  }

  @Override
  public Object visitSuperExpr(Expr.Super expr) {
    int distance = locals.get(expr);
    LoxClass superclass = (LoxClass)environment.getAt(
        distance, "super");

    LoxInstance object = (LoxInstance)environment.getAt(
    distance - 1, "this");
    LoxFunction method = superclass.findMethod(expr.method.lexeme);

      if (method == null) {
        throw new RuntimeError(expr.method,
          "Undefined property '" + expr.method.lexeme + "'.");
    }

    return method.bind(object);
  }

  @Override
  public Object visitThisExpr(Expr.This expr) {
    return lookUpVariable(expr.keyword, expr);
  }

@Override
public Object visitVariableExpr(Variable expr) {
   return lookUpVariable(expr.name, expr);
}

private Object lookUpVariable(Token name, Expr expr) {
    Integer distance = locals.get(expr);
    if (distance != null) {
      return environment.getAt(distance, name.lexeme);
    } else {
      return globals.get(name);
    }
  }
@Override
public Void visitBlockStmt(Block stmt) {
    executeBlock(stmt.statements, new Environment(environment));
    return null;
}



  @Override
  public Void visitClassStmt(Stmt.Class stmt) {

    Object superclass = null;
    if (stmt.superclass != null) {
      superclass = evaluate(stmt.superclass);
      if (!(superclass instanceof LoxClass)) {
        throw new RuntimeError(stmt.superclass.name,
            "Superclass must be a class.");
      }
    }


    environment.define(stmt.name.lexeme, null);
    Map<String, LoxFunction> staticMethods = new HashMap<>();
    for (Stmt.Function method : stmt.staticMethods) {
      LoxFunction function = new LoxFunction(method, environment,
      method.name.lexeme.equals("init"));
      staticMethods.put(method.name.lexeme, function);
    }

    LoxClass staticklass = new LoxClass(null, stmt.name.lexeme + " metaclass", (LoxClass) superclass, staticMethods);

     environment.define(stmt.name.lexeme, null);
    Map<String, LoxFunction> methods = new HashMap<>();
    for (Stmt.Function method : stmt.methods) {
      LoxFunction function = new LoxFunction(method, environment,
      method.name.lexeme.equals("init"));
      staticMethods.put(method.name.lexeme, function);
    }

    LoxClass klass = new LoxClass(staticklass, stmt.name.lexeme, (LoxClass) superclass, methods);

    environment.assign(stmt.name, klass);
    return null;
  }

@Override
public Void visitFunctionStmt(Function stmt) {
  LoxFunction function = new LoxFunction(stmt, environment,
                                           false);
  environment.define(stmt.name.lexeme, function);
  return null;
}


@Override
public Void visitIfStmt(Stmt.If stmt) {
    if (isTruthy(evaluate(stmt.condition))) {
      execute(stmt.thenBranch);
    } else if (stmt.elseBranch != null) {
      execute(stmt.elseBranch);
    }
    return null;
}


@Override
public Void visitReturnStmt(Return stmt) {
    Object value = null;
    if (stmt.value != null) value = evaluate(stmt.value);

    throw new src.LoxCompiler.Return(value);

}

//
@Override
public Void visitVarStmt(Stmt.Var stmt) {
    Object value = null;
    if (stmt.initializer != null) {
      value = evaluate(stmt.initializer);
    }

    environment.define(stmt.name.lexeme, value);
    return null;
}

@Override
public Void visitWhileStmt(Stmt.While stmt) {
  try{
    while (isTruthy(evaluate(stmt.condition))) {
      execute(stmt.body);
    }
    }
    catch (BreakException e)
    {

    }
    return null;
}

@Override
public Void visitBreakStmt(Break stmt) {
  throw new BreakException();
}
}

