import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Library} from "../../models/library.model";
import {filter, map, tap} from "rxjs/operators";
import {Action} from "../../models/action.model";
import {OuterComponent} from "../../models/outer.model";
import {ExecutableComponent} from "../../models/executable.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class LibraryManageService {
  constructor(private http: HttpClient) { }

  getAllLibraries() : Observable<Library[]> {
    return this.http.get<Library[]>("api/library?count=10&offset=1", httpOptions);
  }

  getAllActions(): Observable<Action[]> {
    return this.http.get<Action[]>("api/actions", httpOptions);
  }

  getAllCompounds(): Observable<OuterComponent[]> {
    return this.http.get<OuterComponent[]>("api/compounds", httpOptions);
  }

  createLibrary(library: {}): Observable<any> {
    return this.http.post("api/library", library, httpOptions);
  }

}
