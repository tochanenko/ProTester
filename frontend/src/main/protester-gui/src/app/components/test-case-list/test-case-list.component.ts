import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-test-case-list',
  templateUrl: './test-case-list.component.html',
  styleUrls: ['./test-case-list.component.css']
})
export class TestCaseListComponent implements OnInit {

  public id: number;

  constructor(private route: ActivatedRoute) {
    route.params.subscribe(params => this.id = params['id']);
  }

  ngOnInit(): void {
    console.log(this.id);
  }

}
