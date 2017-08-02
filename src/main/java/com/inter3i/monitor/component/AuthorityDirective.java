package com.inter3i.monitor.component;

import com.inter3i.monitor.common.ApplicationSessionFactory;
import com.inter3i.monitor.common.Constant;
import com.inter3i.monitor.entity.account.Authorization;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewToolContext;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/2 16:03
 */
@org.springframework.stereotype.Component
public class AuthorityDirective extends Directive{

    private static final VelocityEngine velocityEngine = new VelocityEngine();

    public String getName() {
        return "hasPermission";
    }

    public int getType() {
        return BLOCK;
    }

    /**
     *  #hasPermission("1","resource") #end
     */
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        if(node.jjtGetNumChildren() != 3){
            throw new RuntimeException("参数个数有误");
        }
        String id = (String)(node.jjtGetChild(0).value(context));
        String type = (String)(node.jjtGetChild(1).value(context));
        if(StringUtils.isEmpty(id)){
            throw new RuntimeException("没有指定id");
        }

        if(StringUtils.isEmpty(type)){
            throw new RuntimeException("没有指定type");
        }

        if(!type.equalsIgnoreCase(Constant.AUTHORITY_TYPE_RESOURCE) && !type.equalsIgnoreCase(Constant.AUTHORITY_TYPE_MENU)){
            throw new RuntimeException("type非法");
        }

        ViewToolContext contexts = (ViewToolContext)context.getInternalUserContext();
        if(contexts.getRequest() != null && contexts.getResponse() != null){
            Authorization authorization = ApplicationSessionFactory.getAuthorization(contexts.getRequest());
            if(authorization != null){
                boolean hasPermission = false;
                if(type.equalsIgnoreCase(Constant.AUTHORITY_TYPE_RESOURCE)){
                    List<String> authorisedResourceNoList = authorization.getAuthorisedResourceNoList();
                    if(CollectionUtils.isNotEmpty(authorisedResourceNoList)){
                        if(authorisedResourceNoList.contains(id)){
                            hasPermission = true;
                        }
                    }
                }else if(type.equalsIgnoreCase(Constant.AUTHORITY_TYPE_MENU)){
                    List<String> authorisedMenuNoList = authorization.getAuthorisedMenuNoList();
                    if(CollectionUtils.isNotEmpty(authorisedMenuNoList)){
                        if(authorisedMenuNoList.contains(id)){
                            hasPermission = true;
                        }
                    }
                }
                if(hasPermission){
                    ASTBlock blockNode = (ASTBlock)node.jjtGetChild(2);
                    String body_tpl = blockNode.literal();
                    writer.write(renderTemplate(context,body_tpl));
                    return true;
                }
            }
        }
        return false;
    }

    public static String renderTemplate(InternalContextAdapter context,String vimStr){
        StringWriter writer = new StringWriter();
        try {
            velocityEngine.evaluate(context, writer, "", vimStr);
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }//渲染模板
        return writer.toString();
    }
}
