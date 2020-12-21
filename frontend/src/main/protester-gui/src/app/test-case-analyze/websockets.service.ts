import {Injectable} from '@angular/core';
import * as Stomp from '@stomp/stompjs';
import {Client} from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebsocketsService {
  url = '/test-case-list';
  client: Client;

  connect(frame: any): void {
    const socket = new SockJS(this.url);
    this.client = Stomp.over(socket);
    this.client.reconnect_delay = 2000;

    this.getStompClient().connect({}, () => {
        frame();
      },

      () => {
        this.reconnect(frame);
      });
  }

  reconnect(frame: () => void): void {
    setTimeout(() => {
      this.connect(frame);
    }, 2000);
  }

  getStompClient(): Client {
    return this.client;
  }

  disconnectClient(): void {
    if (this.getStompClient()) {
      this.getStompClient().disconnect();
    }
  }
}
