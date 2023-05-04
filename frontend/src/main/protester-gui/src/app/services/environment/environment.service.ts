import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {EnvironmentModel} from '../../models/environment/environment.model';
import {EnvironmentFilterModel} from '../../models/environment/environment-filter.model';
import {EnvironmentResponseModel} from '../../models/environment/environment-response.model';


@Injectable({
  providedIn: 'root'
})
export class EnvironmentService {

  constructor(private http: HttpClient) {
  }

  public create(environment: EnvironmentModel): Observable<EnvironmentModel> {
    return this.http.post<EnvironmentModel>('/api/environment', environment);
  }

  public update(environment: EnvironmentModel): Observable<EnvironmentModel> {
    return this.http.put<EnvironmentModel>('/api/environment', environment);
  }

  public delete(id: number): Observable<void> {
    return this.http.delete<void>(`/api/environment/${id}`);
  }

  public findById(id: number): Observable<EnvironmentModel> {
    return this.http.get<EnvironmentModel>(`/api/environment/${id}`);
  }

  findAllPaginated(projectId: number, filter: EnvironmentFilterModel): Observable<EnvironmentResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('environmentName', filter.environmentName);

    return this.http.get<EnvironmentResponseModel>(`/api/environment/findAllPaginated/${projectId}`, {params});
  }

  public findAll(projectId: number): Observable<EnvironmentModel[]> {
    return this.http.get<EnvironmentModel[]>(`/api/environment/findAll/${projectId}`);
  }


}
