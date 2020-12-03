import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Project} from "../../project/project.model";
import {Observable} from "rxjs";
import {TestCaseModel} from "../../test-case/test-case.model";
import {ProjectFilter} from "../../project/project-filter.model";
import {ProjectResponse} from "../../project/project-response.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};
export class TestCaseService {
  constructor(private http: HttpClient) {
  }

  create(testCase: TestCaseModel): Observable<any> {
    return this.http.post('/api/project/create', project, httpOptions);
  }

  update(testCase: TestCaseModel): Observable<any> {
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

  getFilterById(id: number): Observable<TestCaseModel> {
    return this.http.get<TestCaseModel>(`/api/project/${id}`);
  }
}
