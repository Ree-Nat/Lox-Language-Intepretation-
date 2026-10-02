package src.LoxCompiler;

public class LoxGetter {
    private final Stmt.getSetFunction declaration;
    private final Environment closure;

    LoxGetter(Stmt.getSetFunction declaration, Environment closure)
    {
        this.declaration = declaration;
        this.closure = closure;
    }
    
    LoxGetter bind(LoxInstance instance)
    {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new LoxGetter(declaration, environment);
    }

    Object call(Interpreter interpreter)
    {
        Environment environment = new Environment(closure);
        try
        {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue)
        {
            return returnValue.value;
        }
        return null;
    }
    
}
