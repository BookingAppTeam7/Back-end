package com.booking.BookingApp.web_sockets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Configuration
/*
 * Koristi autokonfiguraciju da uveze potrebne artifakte kako bi se omogucilo slanje poruka preko web soketa.
 */
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	/*
	 * Metoda registruje Stomp (https://stomp.github.io/) endpoint i koristi SockJS JavaScript biblioteku
	 * (https://github.com/sockjs)
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket")

				.setAllowedOriginPatterns("*")

				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/socket-subscriber")
				.enableSimpleBroker("/socket-publisher");

	}


//	@Override
//	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
//		registration.addDecoratorFactory(webSocketHandlerDecoratorFactory());
//	}

//	@Bean
//	public WebSocketHandlerDecoratorFactory webSocketHandlerDecoratorFactory() {
//		return new WebSocketHandlerDecoratorFactory() {
//			@Override
//			public WebSocketHandler decorate(final WebSocketHandler handler) {
//				return new WebSocketHandlerDecorator(handler) {
//					@Override
//					public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//						// Dodaj ovde logiku koja ti je potrebna prilikom uspostavljanja konekcije
//						// Ovo se izvršava nakon uspešne WebSocket konekcije
//						super.afterConnectionEstablished(session);
//					}
//				};
//			}
//		};
//	}
}
