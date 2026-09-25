package src.LoxCompiler;

import java.util.ArrayList;
import java.util.List;

public class IntegerEnvironment extends Environment {
    final IntegerEnvironment enclosing;
    private final List<Object> values = new ArrayList<>();

    IntegerEnvironment()
    {
        enclosing = null;
    }

    IntegerEnvironment(IntegerEnvironment enclosing)
    {
        this.enclosing = enclosing;
    }

    void define(Object value)
    {
        values.add(value);
    }

    Object getAt(int distance, int slot)
    {
        IntegerEnvironment environment = this;
        for(int i = 0; i < distance; i ++)
        {
            environment = environment.enclosing;
        }

        return environment.values.get(slot);
    }

    void assignAt(int distance, int slot, Object value)
    {
        IntegerEnvironment environment = this;
        for (int i = 0; i < distance; i++)
        {
            environment = environment.enclosing;
        }
    }

    @Override 
    public String toString()
    {
        String result = values.toString();
        if (enclosing != null)
        {
            result += " ->" + enclosing.toString();
        }
        return result;
    }
    
}
