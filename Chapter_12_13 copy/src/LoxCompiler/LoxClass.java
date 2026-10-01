package src.LoxCompiler;
import java.util.List;
import java.util.Map;

class LoxClass extends LoxInstance implements LoxCallable {
   private final Map<String, LoxFunction> methods;
   final String name;
   final LoxClass superclass;

  LoxClass(LoxClass staticKlass, String name, LoxClass superclass,
           Map<String, LoxFunction> methods) {
    super(staticKlass);
    this.superclass = superclass;
    this.name = name;
    this.methods = methods;
  }

  @Override
  public String toString() {
    return name;
  }

  public Object callStatic(Interpreter interpreter, List<Object> arguments)
  {
    LoxInstance instance = new LoxInstance(this);
    LoxFunction initializer = findMethod("init");
    if (initializer != null) {
      initializer.bind(instance).call(interpreter, arguments);
    }
    return instance; 
  }

  @Override
  public Object call(Interpreter interpreter,
                     List<Object> arguments) {
    LoxInstance instance = new LoxInstance(this);
    LoxFunction initializer = findMethod("init");
    if (initializer != null) {
      initializer.bind(instance).call(interpreter, arguments);
    }
    return instance;
  }

  @Override
  public int arity() {
        LoxFunction initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
  }

    LoxFunction findMethod(String name) {
    if (methods.containsKey(name)) {
      return methods.get(name);
    }
    if (superclass != null) {
      return superclass.findMethod(name);
    }

    
    return null;
  }
}

