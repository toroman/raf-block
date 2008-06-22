/*
    This file is part of RAF Game Engine.

    RAF Game Engine is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RAF Game Engine is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RAF Game Engine; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package edu.raf.flowchart.scripting;

import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
/**
 * Use JavaScript interpreter implemented in Mustang
 * Unfortunately slower than BSH by factor 2/3
 * @author Srecko Toroman
 *
 */
public class JavaScriptEngine implements IScriptEngine {
	private ScriptEngineManager sem;
	private ScriptEngine engine;
	private Invocable invocable;
	
	public JavaScriptEngine() {
		sem = new ScriptEngineManager();
		engine = sem.getEngineByName("javascript");
		invocable = (Invocable) engine;
	}
	
	public JavaScriptEngine(String sourcePath) throws Exception {
		this();
		source(sourcePath);
	}
	
	public void set(String name, Object value) {
		engine.put(name, value);
	}
	
	public Object get(String name) {
		return engine.get(name);
	}

	public void importClass(String klass) throws Exception {
		engine.eval("importClass(Packages." + klass + ");");
	}

	public void importPackage(String pack) throws Exception {
		engine.eval("importPackage(Packages." + pack + ");");
	}

	public void source(String path) throws Exception {
		engine.eval(new FileReader(path));
	}
	
	@Deprecated
	public Object eval(String expression) throws Exception {
		return engine.eval(expression);
	}
	
	public Object invoke(String name, Object... args) throws Exception {
		return invocable.invokeFunction(name, args);
	}
	
	public Object invokeArrayArgs(String name, Object[] argsArray) throws Exception {
		return invocable.invokeFunction(name, argsArray);
	}

	@Override
	public void clear() {
		engine.setBindings(new SimpleBindings(), ScriptContext.ENGINE_SCOPE);
	}

}
