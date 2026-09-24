package src.LoxCompiler;

import java.util.List;

class LoxFunction implements LoxCallable {
  private final FunctionNodeI declaration;
  private final Environment closure;
    LoxFunction(FunctionNodeI declaration, Environment closure) {
    this.closure = closure;
    this.declaration = declaration;
  }

    @Override
  public Object call(Interpreter interpreter,
                     List<Object> arguments) {
    Environment environment = new Environment(closure);
    for (int i = 0; i < declaration.params().size(); i++) {
      environment.define(declaration.params().get(i).lexeme,
          arguments.get(i));

                try {
        interpreter.executeBlock(declaration.body(), environment);
        } catch (Return returnValue) {
        return returnValue.value;
        }
    }

    interpreter.executeBlock(declaration.body(), environment);
    return null;
  }

    @Override
    public int arity() {
        return declaration.params().size();
    }

    @Override
    public String toString() {
      if(declaration.getName() == null)
      {
        return "Lambda function";
      }
        return "<fn " + declaration.getName().lexeme + ">";
    }

}
    
