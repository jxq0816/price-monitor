package com.inter3i.monitor.component;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.util.OutputConverter;
import com.opensymphony.module.sitemesh.velocity.VelocityDecoratorServlet;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/9 19:00
 */
public class MyVelocityDecoratorServlet extends VelocityDecoratorServlet{

    @Override
    public Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context context) {
        HTMLPage htmlPage = (HTMLPage)request.getAttribute("__sitemesh__page");
        context.put("base", request.getContextPath());
        context.put("req", request);
        context.put("res", response);
        String template;
        if(htmlPage == null) {
            context.put("title", "Title?");
            context.put("body", "<p>Body?</p>");
            context.put("head", "<!-- head -->");
            template = request.getServletPath();
        } else {
            try {
                context.put("title", OutputConverter.convert(htmlPage.getTitle()));
                StringWriter decoratorMapper = new StringWriter();
                htmlPage.writeBody(OutputConverter.getWriter(decoratorMapper));
                context.put("body", decoratorMapper.toString());
                decoratorMapper = new StringWriter();
                htmlPage.writeHead(OutputConverter.getWriter(decoratorMapper));
                context.put("head", decoratorMapper.toString());
                context.put("page", htmlPage);
                DecoratorMapper decoratorMapper1 = this.getDecoratorMapper();
                Decorator decorator = decoratorMapper1.getDecorator(request, htmlPage);
                template = decorator.getPage();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return super.getVelocityView().getTemplate(template,"UTF-8");
    }


    private DecoratorMapper getDecoratorMapper() {
        Factory factory = Factory.getInstance(new Config(this.getServletConfig()));
        DecoratorMapper decoratorMapper = factory.getDecoratorMapper();
        return decoratorMapper;
    }
}
