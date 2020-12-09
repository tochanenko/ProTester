import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {DataSetFilterModel} from "../dataset/dataset-filter.model";
import {DataSet} from "../dataset/dataset.model";
import {DatasetResponseModel} from "../dataset/dataset-response.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class DatasetService {

  constructor(private http: HttpClient) { }

  create(dataset: DataSet): Observable<any> {
    return this.http.post('/api/dataset/create', dataset, httpOptions);
  }

  update(dataset: DataSet): Observable<any> {
    return this.http.post('/api/dataset/update', dataset, httpOptions);
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
