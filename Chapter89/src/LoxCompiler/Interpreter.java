package src.LoxCompiler;

import src.LoxCompiler.Expr.Assign;
import src.LoxCompiler.Expr.Call;
import src.LoxCompiler.Expr.Get;
import src.LoxCompiler.Expr.Logical;
import src.LoxCompiler.Expr.Set;
import src.LoxCompiler.Expr.Super;
import src.LoxCompiler.Expr.This;
import src.LoxCompiler.Expr.Variable;

class Interpreter implements Expr.Visitor<Object> {


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

    if (left instanceof Double && right instanceof Double && operator.type == TokenType.SLASH)
      {
        if ((Double) right == 0)
        {
          throw new RuntimeError(operator, "Divisor can not be 0");
        }
      else
      {
        return;
      }

      };
    
   
    throw new RuntimeError(operator, "Operands must be numbers.");
  }


private boolean isEqual(Object a, Object b) {
    if (a == null && b == null) return true;
    if (a == null) return false;

    return a.equals(b);
  }



void interpret(Expr expression) { 
    try {
      Object value = evaluate(expression);
      System.out.println(stringify(value));
    } catch (RuntimeError error) {
      Lox.runtimeError(error);
    }
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


}

