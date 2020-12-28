import {Component, OnDestroy, OnInit} from '@angular/core';
import {TestCaseResultService} from "../../../services/test-case-result.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DatePipe} from "@angular/common";
import {PageEvent} from "@angular/material/paginator";
import {switchMap} from "rxjs/operators";

@Component({
  selector: 'app-test-case-result-table',
  templateUrl: './test-case-result-table.component.html',
  styleUrls: ['./test-case-result-table.component.css']
})
export class TestCaseResultTableComponent implements OnInit, OnDestroy {

  displayedColumns = ['caseName', 'user', 'status', 'start', 'finish'];
  testCaseResults = [];
  dataSource = [];
  projectId = -1;
  processed = false;

  pageEvent: PageEvent;
  pageIndex = 0;
  pageSize = 5;
  length: number;

  subscriptions = [];

  constructor(
    private testCaseResultService: TestCaseResultService,
    private router: Router,
    private route: ActivatedRoute,
    public datePipe: DatePipe
  ) {
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.params.pipe(
        switchMap(params => {
          this.projectId = params['id'];
          if (this.projectId != undefined) {
            return this.testCaseResultService.getForProject(this.projectId, this.pageSize, this.pageIndex + 1);
          } else {
            return this.testCaseResultService.getForProject('', this.pageSize, this.pageIndex + 1);
          }
        }
        )).subscribe(
        results => {
          let testCaseResults = [];
          results['list'].forEach(result => {
            testCaseResults.push({
              startDate: this.datePipe.transform(result['startDate'], 'short'),
              endDate: this.datePipe.transform(result['endDate'], 'short'),
              caseId: result['runResultId'],
              caseName: result['testCase']['name'],
              userId: result['user']['id'],
              userName: result['user']['username'],
              status: result['status']
            })
          });
          this.length = results['totalItems'];
          this.dataSource = testCaseResults;
          this.processed = true;
        }
      )
    );
  }

  updateList(event?: PageEvent): PageEvent {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.subscriptions.push(
      this.testCaseResultService.getForProject(
        this.projectId == undefined ? '' : this.projectId,
        event.pageSize,
        event.pageIndex + 1
      ).subscribe(
        results => {
          let testCaseResults = [];
          results['list'].forEach(result => {
            testCaseResults.push({
              startDate: this.datePipe.transform(result['startDate'], 'short'),
              endDate: this.datePipe.transform(result['endDate'], 'short'),
              caseId: result['testCase']['id'],
              caseName: result['testCase']['name'],
              userId: result['user']['id'],
              userName: result['user']['username'],
              status: result['status']
            })
          });
          this.length = results['totalItems'];
          this.dataSource = testCaseResults;
          this.processed = true;
        }
      )
    );

    return event;
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}

