package src.LoxCompiler;
import java.util.HashMap;
import java.util.Map;
public class Environment {
  private final Map<String, Object> values = new HashMap<>();
  final Environment enclosing;
  Environment() {
    enclosing = null;
  }

  Environment(Environment enclosing) {
    this.enclosing = enclosing;
  }

  void define(String name, Object value) {
    values.put(name, value);
  }
  Object getAt(int distance, String name) {
    return ancestor(distance).values.get(name);
  }

  Environment ancestor(int distance) {
    Environment environment = this;
    for (int i = 0; i < distance; i++) {
      environment = environment.enclosing; 
    }

    return environment;
  }

  //challenge #2 for chapter 8, throws a runtime error if accessed variable is null
  Object get(Token name) {
    if (values.containsKey(name.lexeme)) {

      if(values.get(name.lexeme) == null)
      {
        throw new RuntimeError(name,
        "Undefined variable '" + name.lexeme + "'.");
      }

      return values.get(name.lexeme);
    }

  

    throw new RuntimeError(name,
        "Undefined variable '" + name.lexeme + "'.");
  }

    void assign(Token name, Object value) {
    if (values.containsKey(name.lexeme)) {
      values.put(name.lexeme, value);
      return;
    }

    throw new RuntimeError(name,
        "Undefined variable '" + name.lexeme + "'.");
  }


}
