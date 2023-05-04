import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Library} from "../../models/library.model";
import {Action} from "../../models/action.model";
import {OuterComponent} from "../../models/outer.model";
import {LibraryFilter} from "../../modules/libraries-menu/libraries/list/library-filter.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class LibraryManageService {
  constructor(private http: HttpClient) { }

  getAllLibraries(filter: LibraryFilter) : Observable<any> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('libraryName', String(filter.name));

      return this.http.get("api/library", {params});
  }

  getLibraryById(id: number): Observable<Library> {
    return this.http.get<Library>("api/library/" + id, httpOptions);
  }

  updateLibrary(library, id): Observable<any> {
    return this.http.put("api/library/" + id, library, httpOptions);
  }

  deleteLibrary(id): Observable<any> {
    return this.http.delete("api/library/" + id, httpOptions);
  }

  getAllActions(): Observable<Action[]> {
    let params = new HttpParams();
    params = params.append('pageSize', '999');
    params = params.append('pageNumber', '1');
    params = params.append('actionName', '');
    return this.http.get<Action[]>("api/actions", {params});
  }

  getAllCompounds(): Observable<OuterComponent[]> {
    let params = new HttpParams();
    params = params.append('pageSize', '999');
    params = params.append('pageNumber', '1');
    params = params.append('compoundName', '');
    params = params.append('loadSteps', 'false');
    return this.http.get<OuterComponent[]>("api/compounds", {params});
  }

  createLibrary(library): Observable<any> {
    return this.http.post("api/library", library, httpOptions);
  }

}
