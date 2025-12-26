package net.swedz.tesseract.neoforge.interfaceproxy;

public interface InterfaceProxyEntry<R>
{
	R resolve(Object[] args);
}
