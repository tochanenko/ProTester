import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Router} from '@angular/router';
import {catchError, tap} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import {AuthService} from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router, private service: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    const token = window.sessionStorage.getItem('token');
    const user = window.sessionStorage.getItem('user');
    if (token && user) {
      authReq = req.clone({
        setHeaders: {
          Authorization: token
        }
      });
    }

    return next.handle(authReq).pipe(tap(() => {
      },
      (err: any) => {
        if (err.status !== 401) {
          return;
        }
        window.sessionStorage.removeItem('token');
        window.sessionStorage.removeItem('user');
        this.router.navigate(['/login']).then();
      }));
  }
}
