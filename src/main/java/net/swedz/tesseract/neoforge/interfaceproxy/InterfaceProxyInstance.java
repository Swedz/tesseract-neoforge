package net.swedz.tesseract.neoforge.interfaceproxy;

public interface InterfaceProxyInstance<P, H extends InterfaceProxyHandler>
{
	Class<P> proxyClass();
	
	P proxy();
	
	H handler();
	
	default InterfaceProxyInstance<P, H> load()
	{
		this.handler().loadValues(this.proxyClass(), this.proxy());
		return this;
	}
}
