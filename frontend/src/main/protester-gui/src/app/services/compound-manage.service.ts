import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Action} from "../models/action.model";
import {OuterComponent} from "../models/outer.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class CompoundManageService {

  constructor(private http: HttpClient) { }

  getAllActions(): Observable<Action[]> {
    return this.http.get<Action[]>("api/actions", httpOptions);
  }

  getAllCompounds(): Observable<OuterComponent[]> {
    return this.http.get<OuterComponent[]>("api/compounds", httpOptions);
  }
}
