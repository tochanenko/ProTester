import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Action} from "../../models/action.model";
import {ActionService} from "../../services/action/action.service";
import {ACTIONS} from "../../mock-actions";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-manage-action',
  templateUrl: './manage-action.component.html',
  styleUrls: ['./manage-action.component.css']
})
export class ManageActionComponent implements OnInit,AfterViewInit {

  selectedAction: Action;

  actions = ACTIONS;

  displayedColumns: string[] = ['name', 'description', 'type','actions'];

  image = 'assets/logo.png';

  dataSource = new MatTableDataSource<Action>(this.actions);


  constructor(private actionService: ActionService){}

  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit(): void {
    this.getHeroes();
  }

  getHeroes(): void {
    this.actionService.getActions()
      .subscribe(action => this.actions = action);
  }

  onRowClicked(row: any) {
    console.log('Row clicked: ', row.id)
  }
}
