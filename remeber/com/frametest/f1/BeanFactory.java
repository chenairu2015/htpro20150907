/**
package com.frametest.f1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w11.bean.BeanConfig;
import org.w11.bean.context.ApplicationContext;
import org.w11.bean.context.support.AbstractXmlApplicationContext;
import org.w11.bean.context.support.ClassPathXmlApplicationContext;
import org.w11.bean.context.support.FileSystemXmlApplicationContext;
import org.w11.bean.util.StringUtils;
import org.w11.bean.web.WebXmlApplicationContext;
import org.w11.bean.web.context.ContextLoader;

public class BeanFactory {
	private static Log log = LogFactory.getLog(BeanFactory.class);
	private static boolean initialized = false;
	private static ApplicationContext applicationContext = null;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String id) {
		if (!initialized) {
			throw new RuntimeException("ComponentFactory没有初始化，请检查是否加载了此配置");
		}
		return applicationContext.getBean(id);
	}

	public static synchronized void initApplicationContextInWebContext(
			ServletContext sc) {
		if (initialized) {
			return;
		}
		log.info("从应用上下文中加载配置 ");
		applicationContext = ContextLoader.initContext(sc);
		initIncluded();
		initialized = true;
		log.info("配置加载完成.");
	}

	public static synchronized void initApplicationContextInWebApp(
			ServletContext sc, String configs) {
		if (initialized) {
			log.error("配置已经加载，跳过");
			return;
		}
		if (configs == null) {
			log.error("配置为空，跳过");
			return;
		}

		String[] xmls = StringUtils.tokenizeToStringArray(configs, ",", true,
				false);

		List realpathList = new ArrayList();
		for (int i = 0; i < xmls.length; i++) {
			String realpath = sc.getRealPath(xmls[i]);
			if (!new File(realpath).canRead()) {
				log.error("配置不存在，跳过 [" + xmls[i] + "].");
			} else {
				realpathList.add(realpath);
			}
		}
		log.info("加载配置 [" + realpathList + "].");
		applicationContext = new WebXmlApplicationContext(
				(String[]) realpathList.toArray(new String[realpathList.size()]));
		((WebXmlApplicationContext) applicationContext).setResourceBasePath(sc
				.getRealPath("/"));
		initIncluded();
		initialized = true;
		log.info("配置加载完成 [" + configs + "].");
	}

	public static synchronized void initApplicationContext(String realPath) {
		initApplicationContextInClassPath(new String[] { realPath });
	}

	public static synchronized void initApplicationContext(String[] realPaths) {
		if (initialized) {
			return;
		}
		log.info("loading ApplicationContext from class path...");
		applicationContext = new FileSystemXmlApplicationContext(realPaths);
		initIncluded();
		initialized = true;
		log.info("ApplicationContext loaded.");
	}

	public static synchronized void initApplicationContextInClassPath(
			String config) {
		initApplicationContextInClassPath(new String[] { config });
	}

	public static synchronized void initApplicationContextInClassPath(
			String[] configs) {
		if (initialized) {
			return;
		}
		log.info("loading ApplicationContext from class path...");
		applicationContext = new ClassPathXmlApplicationContext(configs);
		initIncluded();
		initialized = true;
		log.info("ApplicationContext loaded.");
	}

	private static void initIncluded() {
		if ((applicationContext instanceof AbstractXmlApplicationContext)) {
			String[] map = applicationContext.getBeanDefinitionNames();
			for (int i = 0; i < map.length; i++) {
				Object o = applicationContext.getBean(map[i]);
				if (!(o instanceof BeanConfig))
					continue;
				try {
					BeanConfig cfg = (BeanConfig) o;
					cfg.loadBeanConfig((AbstractXmlApplicationContext) applicationContext);
				} catch (Throwable e) {
					log.error("load included configuration error", e);
				}
			}
		}
	}

	public static synchronized void destoryApplicationContext(ServletContext sc) {
		if (!initialized) {
			return;
		}
		ContextLoader.closeContext(sc);
		applicationContext = null;
		initialized = false;
	}
}
*/