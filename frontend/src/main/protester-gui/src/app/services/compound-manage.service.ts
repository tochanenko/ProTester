import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Action} from "../models/action.model";
import {OuterComponent} from "../models/outer.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {LibraryFilter} from "../components/library-search/library-filter.model";

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

  getAllCompounds(filter: LibraryFilter): Observable<OuterComponent[]> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('projectName', filter.libraryName);
    return this.http.get<OuterComponent[]>("api/compounds", {params});
  }
}
