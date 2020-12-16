import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Project} from '../models/project/project.model';
import {Observable} from 'rxjs';
import {EnvironmentModel} from './environment.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class EnvironmentService {

  constructor(private http: HttpClient) {
  }

  addEnvironment(environment: EnvironmentModel): Observable<any> {
    return this.http.post('/api/environment', environment, httpOptions);
  }

}
