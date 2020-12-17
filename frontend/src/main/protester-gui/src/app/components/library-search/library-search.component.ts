import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PageEvent} from "@angular/material/paginator";
import {Library} from "../../models/library.model";
import {LibraryFilter} from "./library-filter.model";
import {LibraryManageService} from "../../services/library/library-manage.service";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-library-search',
  templateUrl: './library-search.component.html',
  styleUrls: ['./library-search.component.css']
})
export class LibrarySearchComponent implements OnInit {
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
      this.router.navigate(['library/edit'], {queryParams: {id: id}}).then();
    }
  }

  goToView(id): void {
    if (id) {
      this.router.navigate(['library/view'], {queryParams: {id: id}}).then();
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
