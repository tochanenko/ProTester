import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {ProjectFilter} from "../models/project/project-filter.model";
import {Project} from "../models/project/project.model";
import {ProjectResponse} from '../models/project/project-response.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private http: HttpClient) {
  }

  create(project: Project): Observable<any> {
    return this.http.post('/api/project/create', project, httpOptions);
  }

  update(project: Project): Observable<any> {
    return this.http.put('/api/project/update', project, httpOptions);
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

  getPageCount(): Observable<any> {
    return this.http.get<number>('/api/project/countOfProjects');
  }

  changeStatus(id: number): Observable<any> {
    return this.http.put(`/api/project/changeStatus/${id}`, {}, httpOptions);
  }

}
