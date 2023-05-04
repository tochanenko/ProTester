import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class TestCaseResultService {

  constructor(private http: HttpClient) {
  }

  getAllProjects(): Observable<any> {
    return this.http.get("/api/projects");
  }

  getForProject(id: any = '', size: number = null, page: number = null): Observable<any> {
    let params = {};

    if (size != null) {
      params['pageSize'] = size.toString();
    }

    if (page != null) {
      params['pageNumber'] = page.toString();
    }

    return this.http.get('/api/test-case-results/project/' + id, {params: params});
  }
}
