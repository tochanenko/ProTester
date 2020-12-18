import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {ActionFilter} from "../../modules/libraries-menu/actions/action-filter.model";
import {ActionResponse} from "../../modules/libraries-menu/actions/action-response";
import {Action} from "../../modules/libraries-menu/actions/action.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};


@Injectable({
  providedIn: 'root'
})

export class ActionService {


  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  update(actionUpdateResponse: Action): Observable<any> {
    let params = new HttpParams();
    params = params.append('newDescription', String(actionUpdateResponse.description));
    return this.http.put<any>(`/api/actions/${actionUpdateResponse.id}`, httpOptions, {params});
  }

  getAll(filter: ActionFilter): Observable<ActionResponse> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('actionName', filter.actionName);

    return this.http.get<ActionResponse>('/api/actions', {params});
  }
  getAllFiltered(filter: ActionFilter): Observable<ActionResponse> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('actionName', filter.actionName);
    params = params.append('actionType', filter.actionType);

    return this.http.get<ActionResponse>('/api/actions', {params});
  }
  // getActionById(id: number): Observable<Action> {
  //   return of(ACTIONS.filter( action => action.id === id)[0]);
  // }
  getActionById(id: number): Observable<Action> {
    return this.http.get<Action>(`/api/actions/${id}`);
  }
  getPageCount(): Observable<any> {
    return this.http.get<number>('/api/project/countOfProjects');
  }
}
