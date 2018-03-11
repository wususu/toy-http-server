package http;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.google.common.collect.Maps;

/**
 * <p>实例化并管理Servlet对象</p>
 * @author janke
 *
 */
public class ServletUriManager {
	
	public static final String packageName = "http.servlet";
	
	private static Map<String, Class<Servlet>> classCache;
	private static Map<String, Servlet> servletUriCache;
	private static List<String> classNames;
	public static boolean lazeInit = false;
	
	static{
		servletUriCache = Maps.newHashMap();
		classNames = getClassName(packageName);
		classCache = genClassByClassNames(classNames);
		System.out.println(classCache.toString());

		if (!lazeInit) {
			 getBeansWithAnnon(classCache, servletUriCache);
		}
	}
	
	public static boolean exists(String uri){
		return servletUriCache.containsKey(uri);
	}
	
	public static Servlet getServletByUri(String uri){
		return servletUriCache.get(uri);
	}
	
	/**
	 * <p>根据类名放射获取类对象</p>
	 * @author janke
	 * @param classNames
	 * @return
	 */
	private static<T> Map<String, Class<T>> genClassByClassNames(List<String> classNames){
		Map<String, Class<T>> map = Maps.newHashMap();
		for (String className : classNames) {
			try {
				Class clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
				map.put(className, clazz);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * <p>实例化带有@HasherService注解的servlet, 并以uri为key存入hashmap</p>
	 * @author janke
	 * @param clazzs 类对象
	 * @param servletUriCache 
	 * @return
	 */
	private static<T> Map<String, T> getBeansWithAnnon(Map<String, Class<T>> clazzs, Map<String, T> servletUriCache){
		Map<String, T> map = servletUriCache;
		Set<String> keySet = map.keySet();
		for (Class<T> clazz : clazzs.values()) {
			Annotation[] annotations = clazz.getAnnotations();
			for (Annotation annotation : annotations) {
				if (HasherService.class.isInstance(annotation)) {
					HasherService hasherService = (HasherService)clazz.getAnnotation(HasherService.class);
					String uri = hasherService.value();
					if (uri == null || uri.trim().equals("") || !uri.startsWith("/")) {
						throw new RuntimeException("Uri should start with /");
					}
					if (keySet.contains(uri)) {
						throw new RuntimeException("Uri confuse: " + clazz + " | " + uri);
					}
					try {
						Object obj =  clazz.newInstance();
						if (Servlet.class.isInstance(obj)){
							Servlet servlet = (Servlet)obj;
						try {			
								servlet.init(null);
						} catch (ServletException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						map.put(uri, (T)servlet);
					}
					} catch (InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return map;
	}
	
	private static List<String>  getClassName(String packageName){
		return getClassName(packageName, true);
	}
	
	/**
	 * <p>根据包名获取该package下的完整类名</p>
	 * @author janke
	 * @param packageName 包名 
	 * @param trace 是否递归
	 * @return
	 */
	private static List<String> getClassName(String packageName, boolean trace){
		List<String> classNames = null;
		ClassLoader loader = ServletUriManager.class.getClassLoader();
		String packagePath = packageName.replace(".", File.separator);
		URL url = loader.getResource(packagePath);
		if (url != null) {
			String type = url.getProtocol();
			if (type.equals("file")) {
				classNames = getClassNameByFile(packageName, url.getPath(), null, trace);
				return classNames;
			}
		}
		return null;
	}
	
	/**
	 * <p>根据路径获取完整的类名</p>
	 * @author janke
	 * @param packageName 包名
	 * @param filePath class文件目录
	 * @param classNames 初始类名列表, 一开始设为null
	 * @param trace 是否递归遍历子目录
	 * @return
	 */
	private static List<String> getClassNameByFile(String packageName, String filePath, List<String> classNames, boolean trace){
		List<String> myClassName = new LinkedList<>();
		File file = new File(filePath);
		String fatherPath = file.getPath();
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (trace) {
					String childFirst = childFile.getPath();
					String childPackageName=  packageName + "." + childFirst.substring(fatherPath.length()+1, childFirst.length());
					myClassName.addAll(getClassNameByFile(childPackageName, childFile.getPath(), myClassName, trace));
				}
			}else {
				String childFilePath = childFile.getPath();
				if (childFilePath.endsWith(".class")) {
					String className = childFilePath.substring(
							fatherPath.length() + 1, childFilePath.length()-6);
					myClassName.add(packageName + "." + className);
				}
			}
		}
		return myClassName;
	}
	public static void main(String[] args) {
		
	}
}
