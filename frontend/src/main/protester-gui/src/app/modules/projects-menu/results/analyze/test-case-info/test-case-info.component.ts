import {Component, Input, OnInit} from '@angular/core';
import {MatTreeNestedDataSource} from '@angular/material/tree';
import {NestedTreeControl} from '@angular/cdk/tree';
import {ActionResultModel, TestCaseResultModel} from '../../../../../models/run-analyze/result.model';

@Component({
  selector: 'app-test-case-info',
  templateUrl: './test-case-info.component.html',
  styleUrls: ['./test-case-info.component.css']
})
export class TestCaseInfoComponent implements OnInit {

  @Input() testCaseResult: TestCaseResultModel;

  dataSource: MatTreeNestedDataSource<TestCaseResultModel>;
  treeControl: NestedTreeControl<ActionResultModel, ActionResultModel>;

  ngOnInit(): void {
    this.treeControl = new NestedTreeControl<any>(node => node.innerResults);
    this.dataSource = new MatTreeNestedDataSource<TestCaseResultModel>();
    this.dataSource.data = [this.testCaseResult];
  }

  refreshTree(): void {
    const dataToRefresh = this.dataSource.data;
    this.dataSource.data = null;
    this.dataSource.data = dataToRefresh;
  }

  hasChild = (_: number, node: any) => (!!node.innerResults && node.innerResults.length >= 0);
}
