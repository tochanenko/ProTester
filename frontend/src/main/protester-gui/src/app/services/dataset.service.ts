import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import { Injectable } from '@angular/core';
import {Observable, throwError} from "rxjs";
import {DataSetFilterModel} from "../dataset/dataset-filter.model";
import {DataSet} from "../dataset/dataset.model";
import {DatasetResponseModel} from "../dataset/dataset-response.model";
import {catchError} from "rxjs/operators";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class DatasetService {

  constructor(private http: HttpClient) { }

  public create(dataset: DataSet): Observable<any> {
    return this.http.post('/api/dataset/create', dataset, httpOptions);
  }

  public update(dataset: DataSet): Observable<any> {
    return this.http.put('/api/dataset/update', dataset, httpOptions);
  }

  public delete(id: number): Observable<DataSet> {
    return this.http.delete<DataSet>(`/api/dataset/delete/${id}`, httpOptions)
  }

  getAll(filter: DataSetFilterModel): Observable<DatasetResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('datasetName', filter.name);

    return this.http.get<DatasetResponseModel>('/api/dataset', {params});
  }

  getDataSetById(id: number): Observable<DataSet> {
    return this.http.get<DataSet>(`/api/dataset/${id}`);
  }

  getPageCount(): Observable<any> {
    return this.http.get<number>('/api/dataset/countOfDataSets');
  }

}
