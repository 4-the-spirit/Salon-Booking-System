import React, { useEffect, useRef, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { addNotification } from "../../../Redux/Notifications/action";
import NotificationCard from "./NotificationCard";

const WS_URL = "http://localhost:5000/api/notifications/ws";

const Notification = ({ type }) => {
  const dispatch = useDispatch();
  const { auth, notification } = useSelector((store) => store);

  const clientRef = useRef(null);
  const subscriptionRef = useRef(null);

  const onMessageReceive = useCallback(
    (frame) => {
      try {
        const data = JSON.parse(frame.body);
        dispatch(addNotification(data));
      } catch (e) {}
    },
    [dispatch]
  );

  useEffect(() => {
    if (!auth?.user?.id) return;

    const client = new Client({
      webSocketFactory: () => new SockJS(WS_URL),
      reconnectDelay: 5000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      onConnect: () => {
        subscriptionRef.current = client.subscribe(
          `/notification/user/${auth.user.id}`,
          onMessageReceive
        );
      },
      onStompError: () => {},
      onWebSocketClose: () => {},
      debug: () => {},
    });

    clientRef.current = client;
    client.activate();

    return () => {
      try {
        subscriptionRef.current?.unsubscribe();
      } catch (e) {}
      clientRef.current?.deactivate();
      clientRef.current = null;
      subscriptionRef.current = null;
    };
  }, [auth?.user?.id, onMessageReceive]);

  return (
    <div className="flex justify-center px-5 md:px-20 py-5 md:py-10">
      <div className="space-y-5 w-full lg:w-1/2">
        <h1 className="text-2xl font-bold text-center">Notifications</h1>
        {notification.notifications.map((item) => (
          <NotificationCard type={type} key={item.id} item={item} />
        ))}
      </div>
    </div>
  );
};

export default Notification;
