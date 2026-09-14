package LoxCompiler;

import java.util.List;

import LoxCompiler.Expr.Assign;
import LoxCompiler.Expr.Call;
import LoxCompiler.Expr.Get;
import LoxCompiler.Expr.Logical;
import LoxCompiler.Expr.Set;
import LoxCompiler.Expr.Super;
import LoxCompiler.Expr.This;
import LoxCompiler.Expr.Variable;
import LoxCompiler.Stmt.Block;
import LoxCompiler.Stmt.Class;
import LoxCompiler.Stmt.Function;
import LoxCompiler.Stmt.If;
import LoxCompiler.Stmt.Print;
import LoxCompiler.Stmt.Return;
import LoxCompiler.Stmt.Var;
import LoxCompiler.Stmt.While;

class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void>{


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

  private void execute(Stmt stmt) {
    stmt.accept(this);
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

private void checkNumberOperand(Token operator, Object operand) {
    if (operand instanceof Double) return;
    throw new RuntimeError(operator, "Operand must be a number.");
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

private void checkNumberOperands(Token operator,
                                   Object left, Object right) {
    if (left instanceof Double && right instanceof Double) return;
    
    throw new RuntimeError(operator, "Operands must be numbers.");
  }


private boolean isEqual(Object a, Object b) {
    if (a == null && b == null) return true;
    if (a == null) return false;

    return a.equals(b);
  }
@Override 
public Void visitExpressionStmt(Stmt.Expression stmt) {
    evaluate(stmt.expression);
    return null;
  }

@Override
public Object visitAssignExpr(Assign expr) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitAssignExpr'");
}

@Override
public Object visitCallExpr(Call expr) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitCallExpr'");
}

@Override
public Object visitGetExpr(Get expr) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitGetExpr'");
}

@Override
public Object visitLogicalExpr(Logical expr) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitLogicalExpr'");
}

@Override
public Object visitSetExpr(Set expr) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitSetExpr'");
}

@Override
public Object visitSuperExpr(Super expr) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitSuperExpr'");
}

@Override
public Object visitThisExpr(This expr) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitThisExpr'");
}

@Override
public Object visitVariableExpr(Variable expr) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitVariableExpr'");
}

@Override
public Void visitBlockStmt(Block stmt) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitBlockStmt'");
}

@Override
public Void visitClassStmt(Class stmt) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitClassStmt'");
}

@Override
public Void visitFunctionStmt(Function stmt) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitFunctionStmt'");
}

@Override
public Void visitIfStmt(If stmt) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitIfStmt'");
}

@Override
  public Void visitPrintStmt(Stmt.Print stmt) {
    Object value = evaluate(stmt.expression);
    System.out.println(stringify(value));
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

  void interpret(List<Stmt> statements) {
    try {
      for (Stmt statement : statements) {
        execute(statement);
      }
    } catch (RuntimeError error) {
      Lox.runtimeError(error);
    }
  }

@Override
public Void visitReturnStmt(Return stmt) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitReturnStmt'");
}

@Override
public Void visitVarStmt(Var stmt) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitVarStmt'");
}

@Override
public Void visitWhileStmt(While stmt) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'visitWhileStmt'");
}

}

