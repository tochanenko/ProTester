import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {DataSet} from "../modules/projects-menu/datasets/dataset.model";
import {DataSetFilterModel} from "../modules/projects-menu/datasets/dataset-filter.model";
import {DatasetResponseModel} from "../modules/projects-menu/datasets/dataset-response.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class DatasetService {
  constructor(private http: HttpClient) {
  }

  public create(dataset: DataSet): Observable<any> {
    return this.http.post('/api/dataset', dataset, httpOptions);
  }

  public update(dataset: DataSet): Observable<any> {
    console.log(dataset);
    return this.http.put('/api/dataset', dataset, httpOptions);
  }

  public delete(id: number): Observable<DataSet> {
    return this.http.delete<DataSet>(`/api/dataset/${id}`, httpOptions)
  }

  public getAll(filter: DataSetFilterModel): Observable<DatasetResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('dataSetName', filter.name);
    console.log(filter.name);
    return this.http.get<DatasetResponseModel>('/api/dataset', {params});
  }

  public getDataSetById(id: number): Observable<DataSet> {
    return this.http.get<DataSet>(`/api/dataset/${id}`);
  }

  getPageCount(): Observable<any> {
    return this.http.get<number>('/api/dataset/countOfDataSets');
  }

}
