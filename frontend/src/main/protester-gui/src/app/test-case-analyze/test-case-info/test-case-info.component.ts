import {Component, Input, OnInit} from '@angular/core';
import {ActionResult, TestCaseResult} from '../result.model';
import {MatTreeNestedDataSource} from '@angular/material/tree';
import {NestedTreeControl} from '@angular/cdk/tree';

@Component({
  selector: 'app-test-case-info',
  templateUrl: './test-case-info.component.html',
  styleUrls: ['./test-case-info.component.css']
})
export class TestCaseInfoComponent implements OnInit {

  @Input() testCaseResult: TestCaseResult;

  dataSource: MatTreeNestedDataSource<TestCaseResult>;
  treeControl: NestedTreeControl<ActionResult, ActionResult>;

  constructor() {
  }

  ngOnInit(): void {
    this.treeControl = new NestedTreeControl<any>(node => node.innerResults);
    this.dataSource = new MatTreeNestedDataSource<TestCaseResult>();
    this.dataSource.data = [this.testCaseResult];
  }

  refreshTree(): void{
    const dataToRefresh = this.dataSource.data;
    this.dataSource.data = null;
    this.dataSource.data = dataToRefresh;
  }

  hasChild = (_: number, node: any) => (!!node.innerResults && node.innerResults.length > 0);
}
