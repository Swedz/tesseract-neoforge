package net.swedz.tesseract.neoforge.interfaceproxy;

import java.lang.reflect.Proxy;

public abstract class InterfaceProxyManager<H extends InterfaceProxyHandler>
{
	protected abstract <P> H createHandler(Class<P> proxyClass);
	
	protected abstract <P> InterfaceProxyInstance<P, H> createInstance(Class<P> proxyClass, P proxy, H handler);
	
	public <P> InterfaceProxyInstance<P, H> build(Class<P> proxyClass)
	{
		try
		{
			var handler = this.createHandler(proxyClass);
			var proxy = (P) Proxy.newProxyInstance(proxyClass.getClassLoader(), new Class[]{proxyClass}, handler);
			return this.createInstance(proxyClass, proxy, handler);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	public abstract static class WithArgument<H extends InterfaceProxyHandler, A> extends InterfaceProxyManager<H>
	{
		protected abstract A defaultManagerArgument();
		
		@Override
		protected <P> H createHandler(Class<P> proxyClass)
		{
			return this.createHandler(proxyClass, this.defaultManagerArgument());
		}
		
		protected abstract <P> H createHandler(Class<P> proxyClass, A arg);
		
		@Override
		public <P> InterfaceProxyInstance<P, H> build(Class<P> proxyClass)
		{
			return this.build(proxyClass, this.defaultManagerArgument());
		}
		
		public <P> InterfaceProxyInstance<P, H> build(Class<P> proxyClass, A arg)
		{
			try
			{
				var handler = this.createHandler(proxyClass, arg);
				var proxy = (P) Proxy.newProxyInstance(proxyClass.getClassLoader(), new Class[]{proxyClass}, handler);
				return this.createInstance(proxyClass, proxy, handler);
			}
			catch (Throwable ex)
			{
				throw new RuntimeException(ex);
			}
		}
	}
}
