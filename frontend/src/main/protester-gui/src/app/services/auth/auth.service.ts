import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../../models/user.model';
import {StorageService} from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private storageService: StorageService) {
  }

  login(user: any): Observable<any> {
    return this.http.post('/api/signin', user);
  }

  register(user: User): Observable<any> {
    return this.http.post('/api/signup', user, {responseType: 'text'});
  }

  logout(): void {
    window.sessionStorage.removeItem('token');
    window.sessionStorage.removeItem('user');
    this.storageService.setUser(null);
  }

  getRoles(): Observable<any> {
    return this.http.get('/api/roles');
  }
}
