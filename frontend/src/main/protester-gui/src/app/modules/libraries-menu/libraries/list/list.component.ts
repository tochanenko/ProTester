import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Library} from "../../../../models/library.model";
import {PageEvent} from "@angular/material/paginator";
import {Subscription} from "rxjs";
import {LibraryManageService} from "../../../../services/library/library-manage.service";
import {Router} from "@angular/router";
import {LibraryFilter} from "./library-filter.model";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  searchForm: FormGroup;
  dataSource: Library[];
  pageEvent: PageEvent;

  columnsToDisplay: string[] = ['NAME', 'DESCRIPTION', 'CONF'];
  pageSizeOptions: number[] = [5, 10, 25, 50];
  librariesCount = 10;
  libraryFilter: LibraryFilter = new LibraryFilter();

  private subscription: Subscription;

  constructor(private formBuilder: FormBuilder, private libraryService: LibraryManageService, private router: Router) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
    this.searchByFilter();
    this.getLibrariesCount();
  }

  goToEdit(id): void {
    if (id) {
      this.router.navigate([`libraries-menu/libraries/${id}/edit`]).then();
    }
  }

  goToView(id): void {
    if (id) {
      this.router.navigate([`libraries-menu/libraries/${id}`]).then();
    }
  }

  deleteLibraryById(id): void {
    if (id) {
      this.libraryService.deleteLibrary(id).subscribe(() => {
        this.searchByFilter();
      }, error => console.error(error))
    }
  }

  searchByFilter(): void {
    this.subscription = this.libraryService.getAllLibraries(this.libraryFilter).subscribe(data => {
      console.log(data)
      this.dataSource = data["list"];
      this.librariesCount = data["totalItems"];

    });
  }

  getLibrariesCount(): void {
    this.subscription = this.libraryService.getAllLibraries(this.libraryFilter).subscribe( data => {
      this.librariesCount = data["totalItems"];
    });
  }

  onPaginateChange(event): void {
    this.libraryFilter.pageNumber = event.pageIndex + 1;
    this.libraryFilter.pageSize = event.pageSize;
    this.searchByFilter();
  }


  get f() {
    return this.searchForm.controls;
  }

  onFormBlurs(): void {
    this.libraryFilter.name = this.f.search.value;
    this.searchByFilter();
  }

  ngOnDestroy() : void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
