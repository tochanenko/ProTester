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

  getLibrary(name: string) : Observable<any> {
    return this.http.get("api/get_library/${name}", httpOptions);
  }

  searchLibrariesByMatch(firstLetters: string) : Observable<any> {
    return this.http.get<Library[]>("api/get_library?name=${name}", httpOptions);
  }
}
