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

public interface IScriptEngine {
	/**
	 * Load code from source file
	 * 
	 * @param path
	 *            Path to source code (relative or absolute)
	 * @throws Exception
	 *             If something goes wrong (no file, no permissions)
	 */
	public void source(String path) throws Exception;

	/**
	 * Set variable.
	 * 
	 * @param name
	 *            Variable name
	 * @param value
	 *            Value of the variable
	 */
	public void set(String name, Object value);

	/**
	 * Get variable.
	 * 
	 * @param name
	 *            Variable name
	 * @return Returns null if variable not found
	 */
	public Object get(String name);

	/**
	 * Invoke a scripted function "name" with given args.
	 * 
	 * @param name
	 *            Function name
	 * @param args
	 *            Function arguments
	 * @return The result of the invoked function
	 * @throws Exception
	 */
	public Object invoke(String name, Object... args) throws Exception;

	/**
	 * Same as invoke, but arguments are in Object[] array.
	 * 
	 * @param name
	 *            Name of the function
	 * @param argsArray
	 *            Array of Objects to be passed as function arguments
	 * @return Result of the invoked function.
	 */
	public Object invokeArrayArgs(String name, Object[] argsArray) throws Exception;

	/**
	 * Evaluate the expression. (CAUTION: some expressions can't be evaluated on
	 * all engines!)
	 * 
	 * <b>IMPORTANT:</b> This isn't portable across different engines! What is
	 * legal in java script isn't legal in bsh for example (although some syntax
	 * is same)
	 * 
	 * @param expression
	 *            Expression to be evaluated
	 * @return The result of the evaluated expression
	 * @throws Exception
	 */
	public Object eval(String expression) throws Exception;

	/**
	 * Import class (for example java.util.ArrayList).
	 * 
	 * @param klass
	 *            Class to be imported
	 * @throws Exception
	 */
	public void importClass(String klass) throws Exception;

	/**
	 * Import all classes from specified package.
	 * 
	 * @param pack
	 *            Package to be imported ("javax.swing.*");
	 * @throws Exception
	 */
	public void importPackage(String pack) throws Exception;

	/**
	 * Delete all variables.
	 */
	public void clear();

}
