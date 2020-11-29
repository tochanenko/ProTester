import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {ProjectFilter} from "../project/project-filter.model";
import {Project} from "../project/project.model";

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

  getAll(filter: ProjectFilter): Observable<Project[]> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('projectName', filter.projectName);
    params = params.append('projectActive', filter.projectActive);

    return this.http.get<Project[]>('/api/project', {params});
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
