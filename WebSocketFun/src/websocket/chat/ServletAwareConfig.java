package websocket.chat;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ServletAwareConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        Map<String,Object> configProperties = config.getUserProperties();
        if(configProperties!=null) {
        	ServletContext context = httpSession.getServletContext();
        	configProperties.put("servletContext", context);
        	//configProperties.put("httpSession", "testString");
        }
    }

}