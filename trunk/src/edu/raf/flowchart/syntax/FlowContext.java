package edu.raf.flowchart.syntax;

/**
 * Context for evaluation.
 * <p>
 * Has all variables. Though not seen here, usually there is a global context,
 * and local context, where global context is unique, and local contexts can be
 * nested (in function calling) but anyway the context implementation should be
 * aware of the global & local context only.
 * <p>
 * This means that the implementation of local contexes should check the global
 * context after variable couldn't be found in the local :)
 */
public interface FlowContext {
	/**
	 * This should be used by all "execution types" to retrieve a variable,
	 * local or global.
	 * 
	 * 
	 * @param name
	 *            Name of the resource, the *unique* identifier.
	 * @param <T>
	 *            The required type
	 * @param <K>
	 *            The type that method may return (anything that extends <T>)
	 * @param expectedClassType
	 *            Get method will ensure that returned type is of this type
	 *            <p>
	 *            This should be used so that all bad castings can be reported
	 *            from the get method, and not from expressions.
	 * @return
	 */
	public <T, K extends T> K get(String name, Class<T> expectedClassType);

	/**
	 * Same as {@link #get(String)} but for storing values/objects/invocables.
	 * 
	 * @param name
	 * @param value
	 */
	public void put(String name, Object value);

}
