import {Injectable} from '@angular/core';
import {HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor() {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): any {
    let authReq = req;
    const token = window.sessionStorage.getItem('token');
    if (token != null) {
      authReq = req.clone({headers: req.headers.set('Authorization', token)});
    }
    return next.handle(authReq);
  }
}
