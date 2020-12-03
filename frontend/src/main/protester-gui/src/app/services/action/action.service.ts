import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Project} from "../../project/project.model";
import {ProjectFilter} from "../../project/project-filter.model";
import {ProjectResponse} from "../../project/project-response.model";

@Injectable({
  providedIn: 'root'
})

export class ActionService {

  private actionsUrl = 'api/actions';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  getAll(filter: ProjectFilter): Observable<ProjectResponse> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('projectName', filter.projectName);

    return this.http.get<ProjectResponse>('/api/project', {params});
  }

  getAllFiltered(filter: ProjectFilter): Observable<ProjectResponse> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('projectName', filter.projectName);
    params = params.append('projectActive', filter.projectActive);

    return this.http.get<ProjectResponse>('/api/project/filter', {params});
  }

  getActionById(id: number): Observable<Project> {
    return this.http.get<Project>(`/api/project/${id}`);
  }

  getPageCount(): Observable<any> {
    return this.http.get<number>('/api/project/countOfProjects');
  }

}
