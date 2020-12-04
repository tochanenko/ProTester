import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Action} from "../../models/action.model";
import {async, Observable, of} from "rxjs";
import {catchError, tap} from "rxjs/operators";
import {MessageService} from "../message/message.service";
import {ACTIONS} from "../../mock-actions";

@Injectable({
  providedIn: 'root'
})

export class ActionService {

  private actionsUrl = 'api/actions';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient, private messageService: MessageService) {
  }

  private log(message: string): void {
    this.messageService.add(`Hero service: ${message}`);
  }

  private handleError<T>(operation = 'operation', result?: T): any {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      this.log(`${operation} failed`);

      return of(result as T);
    };
  }

  getActions(): Observable<Action[]> {
    // return this.http.get<Action[]>(this.actionsUrl)
    //   .pipe(
    //     tap(_ => this.log('fetched heroes')),
    //     catchError(this.handleError<Action[]>('getActions', []))
    //   );
    return of(ACTIONS);
  }

  getActionById(id: number): Observable<Action> {
    const url = `${this.actionsUrl}/${id}`;
    // return this.http.get<Action>(url).pipe(
    //   tap(_ => this.log(`fetched hero id=${id}`)),
    //   catchError(this.handleError<Action>(`getAction id=${id}`))
    // );
    return of(ACTIONS.find(elem => elem.id === id));
  }

  getActionByName(name: string): Observable<Action> {
    const url = `${this.actionsUrl}/name?name=${name}`;
    return this.http.get<Action>(url).pipe(
      tap(_ => this.log(`fetched hero name=${name}`)),
      catchError(this.handleError<Action>(`getAction name=${name}`))
    );
  }

  getActionByType(type: string): Observable<Action> {
    const url = `${this.actionsUrl}/type?type=${type}`;
    return this.http.get<Action>(url).pipe(
      tap(_ => this.log(`fetched hero name=${type}`)),
      catchError(this.handleError<Action>(`getAction name=${type}`))
    );
  }

  getAll(pageSize: number, pageNumber: number): Observable<Action[]> {
    let params = new HttpParams();
    params = params.append('pageSize', String(pageSize));
    params = params.append('pageNumber', String(pageNumber));

    return this.http.get<Action[]>(`${this.actionsUrl}/page`, {params});

    // }


    // updateAction(action: Action): Observable<Action> {
    //   return this.http.put(this.actionsUrl, action , this.httpOptions).pipe(
    //     tap(_ => this.log(`updated hero with id=${action.id}`)),
    //     catchError(this.handleError<any>('updateHero'))
    //   );
    // }
  }
}
