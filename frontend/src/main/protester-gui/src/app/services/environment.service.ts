import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {EnvironmentModel} from '../models/environment.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class EnvironmentService {

  constructor(private http: HttpClient) {
  }

  public create(environment: EnvironmentModel): Observable<EnvironmentModel> {
    return this.http.post<EnvironmentModel>('/api/environment', environment, httpOptions);
  }

  public update(environment: EnvironmentModel): Observable<EnvironmentModel> {
    return this.http.put<EnvironmentModel>('/api/environment', environment, httpOptions);
  }

  public delete(id: number): Observable<void> {
    return this.http.delete<void>(`/api/environment/${id}`, httpOptions);
  }

  public findById(id: number): Observable<EnvironmentModel> {
    return this.http.get<EnvironmentModel>(`/api/environment/${id}`, httpOptions);
  }

  public findAll(): Observable<EnvironmentModel[]> {
    return this.http.get<EnvironmentModel[]>('/api/environment', httpOptions);
  }
}
