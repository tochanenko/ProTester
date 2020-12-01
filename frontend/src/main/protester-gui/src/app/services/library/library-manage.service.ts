import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Library} from "../../models/library.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class LibraryManageService {
  constructor(private http: HttpClient) { }

  getAllLibraries() : Observable<Library[]> {
    return this.http.get<Library[]>("assets/libraries.json", httpOptions);
  }
}
