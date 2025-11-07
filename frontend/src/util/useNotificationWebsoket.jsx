import React, { useEffect, useRef, useCallback } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { addNotification } from "../Redux/Notifications/action";
import { useDispatch } from "react-redux";

const useNotificationWebsoket = (userId, type) => {
  const dispatch = useDispatch();
  const clientRef = useRef(null);
  const subscriptionRef = useRef(null);

  const onMessageRecive = useCallback(
    (frame) => {
      try {
        const receivedMessage = JSON.parse(frame.body);
        dispatch(addNotification(receivedMessage));
      } catch {}
    },
    [dispatch]
  );

  useEffect(() => {
    if (!userId) return;

    const client = new Client({
      webSocketFactory: () =>
        new SockJS("http://localhost:5000/api/notifications/ws"),
      reconnectDelay: 5000,
      onConnect: () => {
        subscriptionRef.current = client.subscribe(
          `/notification/${type}/${userId}`,
          onMessageRecive
        );
      },
      debug: () => {},
    });

    clientRef.current = client;
    client.activate();

    return () => {
      try {
        subscriptionRef.current?.unsubscribe();
      } catch {}
      clientRef.current?.deactivate();
      clientRef.current = null;
      subscriptionRef.current = null;
    };
  }, [userId, type, onMessageRecive]);
};

export default useNotificationWebsoket;
