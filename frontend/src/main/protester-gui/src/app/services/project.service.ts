import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ProjectFilter} from '../models/project/project-filter.model';
import {Project} from '../models/project/project.model';
import {ProjectResponse} from '../models/project/project-response.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private http: HttpClient) {
  }

  create(project: Project): Observable<Project> {
    return this.http.post<Project>('/api/project', project);
  }

  update(project: Project): Observable<Project> {
    return this.http.put<Project>('/api/project', project);
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

  getProjectById(id: number): Observable<Project> {
    return this.http.get<Project>(`/api/project/${id}`);
  }

  changeStatus(id: number): Observable<any> {
    return this.http.put(`/api/project/changeStatus/${id}`, {});
  }

}
