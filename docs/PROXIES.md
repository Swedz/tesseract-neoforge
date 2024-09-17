# Proxies

Sometimes when making mods you need to access client-only code in an ambiguous environment (it may be client or may be
server). This can get very dangerous if not handled properly, and the simplest solution I've seen is with some kind of
proxy. Tesseract provides its own take on the proxy solution using annotations to register proxies with different
environments.

Below is an overview of each proxy environment and how to use them.

## Creating a Proxy

Making a proxy is relatively straightforward. For a proxy to make any sense, you need at minimum two classes. The first
class will act as your "common" proxy which will always get loaded by default, and then the second class will act as
your "conditional" proxy which will only get loaded in certain environments. It is possible to have more than one
conditional proxy, and a priority can be defined in the annotation. Proxies with a higher priority will override proxies
with lower priorities.

Proxies are loaded and initialized on startup automatically by the Tesseract mod itself. Proxies will not be loaded
at mixin time, but will be loaded once Tesseract is loaded.

Here's an example common proxy that would always get loaded by default. If there is a conditional proxy that extends
this proxy and has its condition met, it will override this one.

```java
@ProxyEntrypoint
public class MyProxy implements Proxy {}
```

Here's an example conditional proxy that only would get loaded on the client side.

```java
@ProxyEntrypoint(environment = ProxyEnvironment.CLIENT)
public class MyClientProxy extends MyProxy {}
```

There's also the option to make a conditional proxy that is only loaded if a certain mod is loaded. Here's an example of
how that would be done:

```java
@ProxyEntrypoint(environment = ProxyEnvironment.MOD, modid = "<mod id>")
public class MyModProxy extends MyProxy {}
```

A proxy entrypoint can also use multiple environments, so if you wanted a proxy that would only be loaded when a mod is
loaded *and* the environment is the client, that can be done too. By using the priority system, you can have multiple
different conditional proxies that are loaded with different conditions.

The `Proxy` interface has one empty method, `init()`, that is called when the proxy is initialized. This method can be
overridden to run code immediately when that proxy is loaded on startup.

## Using a Proxy

Using a proxy is pretty straight forward. Proxies are referenced to by their super-most class other than the `Proxy`
interface itself. Here's an example of how you can grab your proxy and use it:

```java
Proxies.get(MyProxy.class).yourMethodHere();
```