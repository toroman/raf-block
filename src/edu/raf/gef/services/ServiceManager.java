package edu.raf.gef.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager {
	private Map<Class<?>, List<?>> services;

	private List<ServiceManagerListener> listeners = new ArrayList<ServiceManagerListener>(2);

	public ServiceManager() {
		services = new HashMap<Class<?>, List<?>>();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getServiceImplementations(Class<T> service) {
		List<T> implementations = (List<T>) services.get(service);
		if (implementations == null)
			return Collections.emptyList();
		return Collections.unmodifiableList(implementations);
	}

	@SuppressWarnings("unchecked")
	public synchronized <T, Q extends T> void addServiceImplementation(Q implementation,
			Class<T> service) {
		if (!service.isInstance(implementation))
			throw new IllegalArgumentException("Implementation must implement the service!");
		List<T> providers = (List<T>) services.get(service);
		if (providers == null) {
			providers = new ArrayList<T>(2);
			services.put(service, providers);
		}
		providers.add(implementation);
		for (ServiceManagerListener listener : this.listeners) {
			listener.serviceAdded(implementation, service);
		}
	}

	public synchronized void addServiceManagerListener(ServiceManagerListener listener) {
		this.listeners.add(listener);
	}

	public synchronized boolean removeServiceManagerListener(ServiceManagerListener listener) {
		return this.listeners.remove(listener);
	}

	public static interface ServiceManagerListener {
		public <I, T extends I> void serviceAdded(T implementation, Class<I> service);
	}

}
