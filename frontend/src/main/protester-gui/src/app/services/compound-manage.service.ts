import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Action} from "../models/action.model";
import {OuterComponent} from "../models/outer.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {CompoundFilter} from "../modules/libraries-menu/compounds/compound-search/compound-filter.model";

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

  getAllCompoundsWithFilter(filter: CompoundFilter): Observable<OuterComponent[]> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('compoundName', String(filter.compoundName));
    params = params.append('loadSteps', "false");
    return this.http.get<OuterComponent[]>("api/compounds", {params});
  }

  getAllCompounds(): Observable<OuterComponent[]> {

    return this.http.get<OuterComponent[]>("api/compounds", httpOptions);
  }

  getCompoundById(id: number): Observable<OuterComponent>{
    return this.http.get<OuterComponent>(`api/compounds/${id}`, httpOptions);

  }

  createCompound(compound): Observable<any> {
    return this.http.post('api/compounds', compound, httpOptions);
  }

  deleteCompound(compound_id): Observable<any> {
    return this.http.delete(`api/compounds/${compound_id}`, httpOptions);
  }
}
