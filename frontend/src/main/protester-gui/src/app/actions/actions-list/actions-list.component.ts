import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {Action} from "../action.model";
import {ActionService} from "../../services/action/action.service";
import {MatTableDataSource} from "@angular/material/table";
import {ACTIONS} from "../../mock-actions";
import {MatSort} from "@angular/material/sort";
import {ActionUpdateComponent} from "../action-update/action-update.component";

@Component({
  selector: 'app-actions-list',
  templateUrl: './actions-list.component.html',
  styleUrls: ['./actions-list.component.css']
})
export class ActionsListComponent implements OnInit {


  actions = ACTIONS;
  dataSource = new MatTableDataSource<Action>(this.actions);
  pageEvent: PageEvent;
  displayedColumns: string[] = ['NAME', 'DESCRIPTION', 'TYPE','EDIT'];
  @ViewChild(MatPaginator,{static:false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static:false}) sort: MatSort;

  private subscription: Subscription;

  constructor(private actionService: ActionService,
              private router: Router,
              public dialog: MatDialog) {
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  ngOnInit(): void {
  }


  openUpdateDialog(actionId: number): void {

    const updateDialogRef = this.dialog.open(ActionUpdateComponent, {
      height: 'auto',
      width: '50%',
      data: {id: actionId}
    });

    this.subscription = updateDialogRef.afterClosed().subscribe(() => {
      // this.searchProjects();
    });
  }

  onNavigate(productCode){
    console.log(`product code ${productCode}`)
  }

}
